package model.business.piv;

import java.util.ArrayList;
import java.util.List;
import model.business.equipe.Equipe;
import model.business.indicador.Indicador;
import model.business.indicador.extra.AtingimentoPiv;
import model.business.indicador.extra.IndicadorNome;
import model.business.regua.ReguaFactory;

import model.entitiy.IndicadoresOperador;
import model.viewmodel.MensagemPiv;
import model.viewmodel.SimuladorAtendimento;

public class CalculoPivFacade {

    private IndicadoresOperador op;
    private List<Indicador> indicadores;
    private List<MensagemPiv> mensagens;
    private Double pontos;
    private Double pesos;
    private Double target;

    private CalcPivStrategyInt calc;

    public CalculoPivFacade(IndicadoresOperador op, List<Indicador> indicadores) {
        this.op = op;
        this.indicadores = indicadores;
        this.pontos = 0d;
        this.pesos = 0d;
        this.target = 0d;
        this.mensagens = new ArrayList<>();
    }

    public CalculoPivFacade(IndicadoresOperador op, List<Indicador> indicadores, CalcPivStrategyInt calc) {
        this.op = op;
        this.indicadores = indicadores;
        this.pontos = 0d;
        this.pesos = 0d;
        this.target = 0d;
        this.mensagens = new ArrayList<>();
        this.calc = calc;
    }

    public void calc() {
        this.calc.calcular(this);
    }

    /**
     * CASO DE USO - SIMULAR PIV
     */
    public void calcular() {

        for (Indicador indicador : indicadores) {

            this.pesos += indicador.getPeso();

            try {
                indicador.calcularRealizado(op);
            } catch (Exception e) {
                indicador.setRealizado(0d);
            }

            try {

                // Regua Atingimento
                ReguaFactory factory = new ReguaFactory(Equipe.buscarPorNome(op.getEquipe()), indicador);
                indicador.setRegua(factory.getRegua());
                // Meta
                indicador.obterMeta();
                // Atingimento
                Double a = indicador.calcularAtingimento(indicador, op);
                indicador.setAtingimento(a);

                this.pontos += indicador.getPontos();
            } catch (Exception e) {
                //System.out.println(e.getMessage());
            }
        }

        // Faltas
        try {
            abateAbsAtingimentoPiv(this.op.getFaltas());
        } catch (Exception e) {
            // System.out.println(e.getMessage());
        }

        this.setTarget(AtingimentoPiv.calcularTarget(pontos));
    }

    public void calcularComRealizado(SimuladorAtendimento s) {

        for (Indicador indicador : indicadores) {

            indicador = adapter(indicador, s);

            this.pesos += indicador.getPeso();

            try {

                // Regua Atingimento
                ReguaFactory factory = new ReguaFactory(Equipe.buscarPorNome(op.getEquipe()), indicador);
                indicador.setRegua(factory.getRegua());
                // Meta
                indicador.obterMeta();
                // Atingimento
                Double a = indicador.calcularAtingimento(indicador, op);
                indicador.setAtingimento(a);

                this.pontos += indicador.getPontos();
            } catch (Exception e) {
                // System.out.println(e.getMessage());
            }
        }

        // Faltas
        try {
            abateAbsAtingimentoPiv(s.getFaltas());
        } catch (Exception e) {
            // System.out.println(e.getMessage());
        }

        this.setTarget(AtingimentoPiv.calcularTarget(pontos));
    }

    public Double getDescontoAbatimentoAbs(Integer faltas) {

        if (faltas > 0) {

            switch (faltas) {
                case 1:
                    return 0.3d;
                case 2:
                    return 0.6d;
                case 3:
                    return 2d;
                default:
                    return 2d;
            }
        }
        return 0d;
    }

    /**
     * Validação de Faltas
     *
     * @param faltas
     */
    public void abateAbsAtingimentoPiv(Integer faltas) {

        if (faltas != null) {
            Double desconto = getDescontoAbatimentoAbs(faltas);

            if (desconto > 0) {
                this.pontos -= desconto;
                Double frm = desconto * 100;
                this.mensagens.add(new MensagemPiv("Desconto de " + frm.intValue() + "% aplicado ao atingimento devido ao número de faltas: " + faltas + "."));
            }
        }
    }

    protected Indicador adapter(Indicador indicador, SimuladorAtendimento s) {

        if (indicador.getNome().equals(IndicadorNome.ADERENCIA)) {
            indicador.setRealizado(s.getAdr().getRealizado());
        }

        if (indicador.getNome().equals(IndicadorNome.FCR)) {
            indicador.setRealizado(s.getFcr().getRealizado());
        }

        if (indicador.getNome().equals(IndicadorNome.GPS)) {
            indicador.setRealizado(s.getGps().getRealizado());
        }

        if (indicador.getNome().equals(IndicadorNome.MONITORIA)) {
            indicador.setRealizado(s.getMonitoria().getRealizado());
        }
        if (indicador.getNome().equals(IndicadorNome.TMA)) {
            indicador.setRealizado(s.getTma().getRealizado());
        }

        return indicador;
    }

    public Double getPesos() {
        return pesos;
    }

    public void setPesos(Double pesos) {
        this.pesos = pesos;
    }

    public Double getTarget() {
        return target;
    }

    public void setTarget(Double target) {
        this.target = target;
    }

    public CalcPivStrategyInt getCalc() {
        return calc;
    }

    public void setCalc(CalcPivStrategyInt calc) {
        this.calc = calc;
    }

    public IndicadoresOperador getOp() {
        return op;
    }

    public void setOp(IndicadoresOperador op) {
        this.op = op;
    }

    public List<Indicador> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(List<Indicador> indicadores) {
        this.indicadores = indicadores;
    }

    public Double getPontos() {
        return pontos;
    }

    public void setPontos(Double pontos) {
        this.pontos = pontos;
    }

}
