package br.com.attus.processos.consumidor.adapter.in.kafka;

import br.com.attus.processos.consumidor.adapter.out.persistence.*;
import br.com.attus.processos.nucleo.dominio.entidade.*;
import br.com.attus.processos.nucleo.dominio.event.ParteCommand;
import br.com.attus.processos.nucleo.dominio.vo.Contato;
import br.com.attus.processos.nucleo.dominio.vo.DocumentoFiscal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class ParteCommandListener {

    private final SpringDataParteRepository parteRepo;
    private final SpringDataProcessoRepository procRepo;

    @KafkaListener(topics = "parte.commands", groupId = "processo")
    @Transactional
    public void onCommand(ParteCommand cmd) {
        switch (cmd) {
            case ParteCommand.Create c -> handleCreate(c);
            case ParteCommand.UpdateContato u -> handleUpdateContato(u);
        }
    }

    /* ------------------------------------------------------------------ */

    private void handleCreate(ParteCommand.Create c) {
        Processo processo = procRepo.findById(c.processoId())
                .orElseThrow(() -> new IllegalArgumentException("Processo não encontrado"));

        DocumentoFiscal doc = new DocumentoFiscal(c.cpfCnpj());
        Contato contato = new Contato(c.email(), c.telefone());

        Parte parte = new Parte(processo, c.nome(), doc, c.tipo(), contato);
        parteRepo.saveAndFlush(parte);
        log.info("Parte criada id={} proc={}", parte.getId(), c.processoId());
    }

    private void handleUpdateContato(ParteCommand.UpdateContato u) {
        Parte parte = parteRepo.findById(u.parteId())
                .orElseThrow(() -> new IllegalArgumentException("Parte não encontrada"));
        parte.setContato(new Contato(u.email(), u.telefone()));
        parteRepo.flush();
        log.info("Contato da parte {} atualizado", parte.getId());
    }
}
