package model.business.indicador;

import model.business.realizado.RealizadoCalcInterface;

import model.entitiy.IndicadoresOperador;

/**
 *
 * @author G0042204
 */
public class IndicadorFcr extends Indicador implements RealizadoCalcInterface {

    public IndicadorFcr() {
        this.setNome(IndicadorNome.Fcr);
    }

    public IndicadorFcr(Double peso) {
        super();
        this.setNome(IndicadorNome.Fcr);
        this.setPeso(peso);
    }

    @Override
    public void calcularRealizado(IndicadoresOperador op) throws Exception {
        if (op.getChamadasFCR() == null || op.getRechamadasFCR() == null) {
            throw new Exception("Indicadores não preenchidos.");
        }
        Double percent = 1 - ((op.getChamadasFCR() / op.getRechamadasFCR()) / 100d);
        this.setRealizado(percent);
    }

}