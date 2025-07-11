package br.com.attus.processos.consumidor.adapter.in.kafka;

import br.com.attus.processos.consumidor.adapter.out.persistence.SpringDataAcaoRepository;
import br.com.attus.processos.consumidor.adapter.out.persistence.SpringDataProcessoRepository;
import br.com.attus.processos.nucleo.dominio.entidade.Acao;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.event.AcaoCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class AcaoCommandListener {

    private final SpringDataAcaoRepository acaoRepo;
    private final SpringDataProcessoRepository procRepo;

    @KafkaListener(topics = "acao.commands", groupId = "processo")
    @Transactional
    public void onCommand(AcaoCommand cmd) {
        if (cmd instanceof AcaoCommand.Create c) handleCreate(c);
    }

    /* --------------------------------------------------------------------- */

    private void handleCreate(AcaoCommand.Create c) {
        Processo processo = procRepo.findById(c.processoId())
                .orElseThrow(() -> new IllegalArgumentException("Processo não encontrado"));

        Acao nova = new Acao(processo, c.tipo(), c.dataRegistro(), c.descricao());
        acaoRepo.saveAndFlush(nova);
        log.info("Ação criada id={} processo={}", nova.getId(), processo.getId());
    }
}
