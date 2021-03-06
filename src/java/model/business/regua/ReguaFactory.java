/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.business.regua;

import java.util.List;
import model.business.equipe.Equipe;
import model.business.indicador.Indicador;
import model.business.indicador.IndicadorAderencia;
import model.business.indicador.IndicadorFcr;
import model.business.indicador.IndicadorMonitoria;
import model.business.indicador.IndicadorTma;
import model.business.indicador.extra.ReguaAtingimento;

/**
 *
 * @author G0042204
 */
public class ReguaFactory {

    private Equipe equipe;

    private Indicador ind;

    private ReguaMaker factory;

    public ReguaFactory(Equipe equipe, Indicador ind) {
        this.equipe = equipe;
        this.ind = ind;

        if (ind instanceof IndicadorFcr) {
            factory = new ReguaIndicadorFcrFactory(ind, equipe);
        } else if (ind instanceof IndicadorAderencia) {
            factory = new ReguaIndicadorAderenciaFactory(ind, equipe);
        } else if (ind instanceof IndicadorMonitoria) {
            factory = new ReguaIndicadorMonitoriaFactory(ind, equipe);
        } else if (ind instanceof IndicadorTma) {
            factory = new ReguaIndicadorTmaFactory(ind, equipe);
        }

    }

    public List<ReguaAtingimento> getRegua() {
        return factory.getRegua();
    }

    public Double getMeta() {
        for (ReguaAtingimento r : factory.getRegua()) {
            if (r.getAtingimento().equals(1d)) {
                return r.getRealizado();
            }
        }
        return 0d;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public Indicador getInd() {
        return ind;
    }

    public void setInd(Indicador ind) {
        this.ind = ind;
    }

    public ReguaMaker getFactory() {
        return factory;
    }

    public void setFactory(ReguaMaker factory) {
        this.factory = factory;
    }

}
