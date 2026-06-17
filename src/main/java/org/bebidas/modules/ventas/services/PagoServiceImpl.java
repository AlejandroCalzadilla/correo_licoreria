package org.bebidas.modules.ventas.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.creditos.Credito;
import org.bebidas.modules.creditos.services.interfaces.CreditoService;
import org.bebidas.modules.pagos.Pago;
import org.bebidas.modules.pagos.repostiories.PagoDAO;
import org.bebidas.modules.service.interfaces.PagoService;
import org.bebidas.modules.service.interfaces.VentaService;
import org.bebidas.modules.ventas.Venta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PagoServiceImpl extends GenericServiceImpl<Pago, Long> implements PagoService {

    private static final BigDecimal ONE_CENT = new BigDecimal("0.01");

    private final PagoDAO pagoDAO;
    private VentaService ventaService;
    private CreditoService creditoService;

    public PagoServiceImpl(PagoDAO pagoDAO) {
        super(pagoDAO);
        this.pagoDAO = pagoDAO;
    }

    public void setVentaService(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    public void setCreditoService(CreditoService creditoService) {
        this.creditoService = creditoService;
    }

    @Override
    public List<Pago> buscarPorVenta(Long ventaId) {
        return pagoDAO.buscarPorVenta(ventaId);
    }

    @Override
    public List<Pago> buscarPorEstado(String estado) {
        return pagoDAO.buscarPorEstado(estado);
    }

    @Override
    public List<Pago> buscarPorTipoPago(String tipoPago) {
        return pagoDAO.buscarPorTipoPago(tipoPago);
    }

    @Override
    public List<Pago> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return pagoDAO.buscarPorRangoFechas(inicio, fin);
    }

    @Override
    public List<Pago> buscarPorCliente(Long clienteId) {
        return pagoDAO.buscarPorCliente(clienteId);
    }

    @Override
    public BigDecimal obtenerTotalPagosPorVenta(Long ventaId) {
        return pagoDAO.obtenerTotalPagosPorVenta(ventaId);
    }

    @Override
    public Pago registrarPago(Pago pago) {
        // Validar que la venta existe
        Venta venta = ventaService.findById(pago.getVenta().getId())
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));

        // Validar que el monto no sea negativo
        if (pago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero");
        }
        /*
         * if( pago.getMonto() != venta.getSaldo() ){
         * throw new
         * IllegalArgumentException("El monto del pago no puede ser diferente al saldo de la venta"
         * );
         * }
         */

        // Establecer fecha y estado por defecto
        pago.setFechaPago(LocalDateTime.now());
        String nroPago = generarSiguienteNroPago();
        pago.setNroPago(nroPago);
        pago.setEstado("completado");
        // Guardar el pago
        Pago pagoGuardado = save(pago);
        // Actualizar el estado de la venta si es necesario
        actualizarEstadoVenta(venta);

        return pagoGuardado;
    }

    @Override
    public Pago procesarPagoVenta(Long ventaId, String tipoPago, BigDecimal monto, String nombrePersona, String email) {
        if (ventaService == null || creditoService == null) {
            throw new IllegalStateException("Dependencias de negocio no configuradas en PagoService");
        }

        Venta venta = ventaService.findById(ventaId).orElse(null);
        if (venta == null) {
            throw new IllegalArgumentException("Venta no encontrada con ID: " + ventaId);
        }

        if (venta.getTipo() != null && venta.getTipo().equals("credito")) {
            Credito credito = creditoService.findAll().stream()
                    .filter(c -> c.getVenta().getId().equals(ventaId))
                    .findFirst()
                    .orElse(null);
            if (credito != null) {
                int cuotasRestantes = Integer.parseInt(credito.getNumeroCuotas());
                if (cuotasRestantes <= 0) {
                    throw new IllegalArgumentException("La venta a crédito ya no tiene cuotas pendientes");
                }
                BigDecimal saldoCreditoActual = normalizeAmount(credito.getSaldo());
                int cuotasIniciales = Integer.parseInt(venta.getNumeroCuotas());
                if (cuotasIniciales <= 0) {
                    throw new IllegalArgumentException("Número de cuotas inválido para la venta");
                }
                List<BigDecimal> planCompletoCuotas = construirPlanPagosCredito(credito.getMontoTotal(),
                        cuotasIniciales);
                int numeroCuotaActual = (cuotasIniciales - cuotasRestantes) + 1;
                if (numeroCuotaActual <= 0 || numeroCuotaActual > cuotasIniciales) {
                    throw new IllegalArgumentException("No se pudo determinar la cuota actual del crédito");
                }
                boolean ultimaCuota = cuotasRestantes == 1;
                BigDecimal montoEsperado = ultimaCuota
                        ? saldoCreditoActual
                        : planCompletoCuotas.get(numeroCuotaActual - 1);

                if (normalizeAmount(monto).compareTo(montoEsperado) != 0) {
                    throw new IllegalArgumentException(
                            "RESTRICCIÓN: Para ventas a crédito, el pago debe ser exactamente "
                                    + (ultimaCuota ? "el saldo restante (" + montoEsperado + ")"
                                            : "el monto de la cuota " + numeroCuotaActual + " (" + montoEsperado
                                                    + ")")
                                    + ". Plan completo: " + formatearPlanPagos(planCompletoCuotas));
                }
            } else {
                throw new IllegalArgumentException("Crédito no encontrado para la venta");
            }
        }

        Pago pago = new Pago();
        pago.setVenta(venta);
        pago.setTipoPago(tipoPago);
        pago.setMonto(monto);
        pago.setNombrePersona(nombrePersona);
        pago.setEmail(email);
        pago.setEstado("pendiente");
        pago.setFechaPago(LocalDateTime.now());
        pago.setCreatedAt(LocalDateTime.now());
        pago.setUpdatedAt(LocalDateTime.now());
        // estado::text = ANY (ARRAY['pendiente'::character varying,
        // 'procesando'::character varying, 'completado'::character varying,
        // 'rechazado'::character varying, 'cancelado'::character varying]::text[])
        Pago pagoCreado = registrarPago(pago);

        BigDecimal nuevoSaldo = normalizeAmount(venta.getSaldo().subtract(monto));
        if (isResidualCentValue(nuevoSaldo)) {
            nuevoSaldo = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        venta.setSaldo(nuevoSaldo);
        ventaService.save(venta);

        if (venta.getTipo() != null && venta.getTipo().equals("credito")) {
            Credito credito = creditoService.findAll().stream()
                    .filter(c -> c.getVenta().getId().equals(ventaId))
                    .findFirst()
                    .orElse(null);

            if (credito != null) {
                System.out.println("Credito encontrado: " + credito);
                BigDecimal nuevoSaldoCredito = normalizeAmount(credito.getSaldo().subtract(monto));
                int nuevasCuotas = Math.max(0, Integer.parseInt(credito.getNumeroCuotas()) - 1);
                if (isResidualCentValue(nuevoSaldoCredito) || nuevasCuotas == 0) {
                    nuevoSaldoCredito = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
                }

                credito.setSaldo(nuevoSaldoCredito);
                credito.setNumeroCuotas(String.valueOf(nuevasCuotas));
                creditoService.save(credito);
                System.out.println("Credito guardado: " + credito);
                if (nuevasCuotas == 0 || nuevoSaldoCredito.compareTo(BigDecimal.ZERO) == 0) {
                    venta.setEstado("completado");
                    venta.setEstadoPago("completado");
                    venta.setSaldo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                    ventaService.save(venta);
                }
            }
        }

        return pagoCreado;
    }

    @Override
    public void anularPago(Long pagoId) {
        Pago pago = findById(pagoId)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado"));

        // Solo se pueden anular pagos pendientes o confirmados
        if (!pago.getEstado().equals("PAGO_COMPLETADO") && !pago.getEstado().equals("CONFIRMADO")) {
            throw new IllegalStateException("Solo se pueden anular pagos pendientes o confirmados");
        }

        // Marcar como anulado
        pago.setEstado("ANULADO");
        save(pago);

        // Actualizar el estado de la venta
        Venta venta = pago.getVenta();
        actualizarEstadoVenta(venta);
    }

    private void actualizarEstadoVenta(Venta venta) {
        BigDecimal totalVenta = venta.getMontoTotal();
        BigDecimal totalPagado = obtenerTotalPagosPorVenta(venta.getId());

        String nuevoEstado;
        if (totalPagado.compareTo(BigDecimal.ZERO) == 0) {
            nuevoEstado = "PENDIENTE";
        } else if (totalPagado.compareTo(totalVenta) < 0) {
            nuevoEstado = "PARCIAL";
        } else if (totalPagado.compareTo(totalVenta) == 0) {
            nuevoEstado = "COMPLETO";
        } else {
            nuevoEstado = "EXCEDENTE";
        }

        if (!venta.getEstado().equals(nuevoEstado)) {
            venta.setEstado(nuevoEstado);
            ventaService.save(venta);
        }
    }

    private String generarSiguienteNroPago() {
        try {
            List<Pago> pagos = this.findAll();
            int maxNumero = 0;
            for (Pago p : pagos) {
                if (p.getNroPago() != null && p.getNroPago().startsWith("P-")) {
                    try {
                        int numero = Integer.parseInt(p.getNroPago().substring(2));
                        if (numero > maxNumero) {
                            maxNumero = numero;
                        }
                    } catch (NumberFormatException e) {
                        // Ignorar si no es válido
                    }
                }
            }
            int siguiente = maxNumero + 1;
            return "P-" + String.format("%06d", siguiente);
        } catch (Exception e) {
            return "P-000001";
        }
    }

    private BigDecimal normalizeAmount(BigDecimal amount) {
        if (amount == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    private boolean isResidualCentValue(BigDecimal amount) {
        return amount.abs().compareTo(ONE_CENT) <= 0;
    }

    private List<BigDecimal> construirPlanPagosCredito(BigDecimal montoTotal, int numeroCuotas) {
        if (numeroCuotas <= 0) {
            throw new IllegalArgumentException("El número de cuotas debe ser mayor a cero");
        }

        BigDecimal totalNormalizado = normalizeAmount(montoTotal);
        int escalaCuota = totalNormalizado.stripTrailingZeros().scale() <= 0 ? 0 : 2;
        totalNormalizado = totalNormalizado.setScale(escalaCuota, RoundingMode.HALF_UP);

        BigDecimal cuotaBase = totalNormalizado.divide(BigDecimal.valueOf(numeroCuotas), escalaCuota,
                RoundingMode.DOWN);
        List<BigDecimal> cuotas = new ArrayList<>(numeroCuotas);

        for (int i = 1; i <= numeroCuotas; i++) {
            if (i == numeroCuotas) {
                BigDecimal acumulado = cuotaBase.multiply(BigDecimal.valueOf(numeroCuotas - 1));
                BigDecimal ultimaCuota = totalNormalizado.subtract(acumulado).setScale(escalaCuota,
                        RoundingMode.HALF_UP);
                cuotas.add(ultimaCuota);
            } else {
                cuotas.add(cuotaBase);
            }
        }

        return cuotas;
    }

    private String formatearPlanPagos(List<BigDecimal> planCuotas) {
        StringBuilder plan = new StringBuilder();
        for (int i = 0; i < planCuotas.size(); i++) {
            if (i > 0) {
                plan.append(", ");
            }
            plan.append("cuota ").append(i + 1).append(" = ").append(planCuotas.get(i));
        }
        return plan.toString();
    }
}
