/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portalefika.comunicao.controller;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.view.Results;
import java.util.Calendar;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import portalefika.comunicao.dal.BannerDAO;
import portalefika.comunicao.entidades.Banner;
import portalefika.comunicao.entidades.BannerLocal;
import portalefika.comunicao.entidades.Conteudo;
import portalefika.controller.AbstractController;

/**
 *
 * @author G0034481
 */
@Controller
@RequestScoped
public class BannerController extends AbstractController {

    @Inject
    private BannerDAO bannerDAO;

    public BannerController() {
    }

    public void create() {
    }

    @Get
    @Path("/comunicacao/banner/listaEsp/{banner.id}")
    public void visualiza(Banner banner) {
        Banner b = (Banner) bannerDAO.buscarPorId(banner);
        if (b != null) {
            this.includeSerializer(b);
        }
    }

    @Post
    @Consumes("application/json")
    @Path("/comunicacao/banner/cadastrar")
    public void adiciona(Banner banner) {
        try {
            Calendar calendar = Calendar.getInstance();
            banner.setDataCriacao(calendar);
            this.bannerDAO.cadastrar(banner);
            this.includeSerializer(banner);
        } catch (Exception e) {
            this.result.use(Results.json()).from(e).serialize();
        }
    }

    @Post
    @Consumes("application/json")
    @Path("/comunicacao/banner/modificar")
    public void modifica(Banner banner) {
        try {
            this.bannerDAO.editar(banner);
            this.includeSerializer(banner);
        } catch (Exception e) {
            this.result.use(Results.json()).from(e).serialize();
        }
    }

    @Get
    @Path("/comunicacao/banner/lista")
    public void lista() {
        List<Banner> l = this.bannerDAO.listarTodos();
        if (l != null) {
            this.includeSerializer(l);
        }
    }

    @Post
    @Consumes("application/json")
    @Path("/comunicacao/banner/excluir")
    public void excluir(Banner banner) {
        try {
            this.bannerDAO.excluir(banner);
            this.includeSerializer(banner);
        } catch (Exception e) {
            this.result.use(Results.json()).from(e).serialize();
        }
    }

    @Get
    @Path("/comunicacao/banner/listaBannerLocal")
    public void listaBannerLocal() {

        result.use(Results.json()).from(BannerLocal.values()).serialize();

    }

    protected void includeSerializer(Object a) {
        result.use(Results.json()).from(a).include("conteudo").serialize();
    }

}