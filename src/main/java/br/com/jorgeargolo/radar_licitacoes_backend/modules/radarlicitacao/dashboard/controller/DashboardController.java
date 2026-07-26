package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.dashboard.controller;

import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.dashboard.service.IDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * Controller responsável por fornecer os indicadores consolidados (KPIs) do painel principal.
 */
@RestController
@RequestMapping("/api/v1/radar-licitacao/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Endpoints para consulta de indicadores consolidados (KPIs)")
public class DashboardController {

    private final IDashboardService dashboardService;

    /**
     * Retorna os indicadores consolidados (KPIs) para o painel principal.
     *
     * @return ResponseEntity contendo os KPIs agregados da prefeitura.
     */
    @GetMapping("/kpis")
    @Operation(summary = "Obter KPIs do dashboard", description = "Retorna os indicadores consolidados: total de produtos, compras, fornecedores e valor total")
    public ResponseEntity<?> obterKpis() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
                .body(dashboardService.obterKpis());
    }
}
