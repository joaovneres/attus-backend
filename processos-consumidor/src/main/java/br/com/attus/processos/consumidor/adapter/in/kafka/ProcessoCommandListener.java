package br.com.attus.processos.consumidor.adapter.in.kafka;

import br.com.attus.processos.consumidor.adapter.out.persistence.SpringDataProcessoRepository;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.event.ProcessoCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class ProcessoCommandListener {

    private final SpringDataProcessoRepository repo;

    @KafkaListener(topics = "processo.commands", groupId = "processo")
    @Transactional
    public void onCommand(ProcessoCommand cmd) {

        switch (cmd) {
            case ProcessoCommand.Create c -> handleCreate(c);
            case ProcessoCommand.UpdateDesc u -> handleUpdate(u);
            case ProcessoCommand.ChangeStatus s -> handleStatus(s);
        }
    }

    /* --------------------------------------------------------------------- */

    private void handleCreate(ProcessoCommand.Create c) {
        repo.findByNumero(c.numero()).ifPresent(p -> {
            throw new IllegalStateException("Processo já existe: " + p.getNumero());
        });

        Processo novo = new Processo(c.numero(), c.dataAbertura(), c.descricao());
        repo.save(novo);
        log.info("Processo criado id={} numero={}", novo.getId(), novo.getNumero());
    }

    private void handleUpdate(ProcessoCommand.UpdateDesc u) {
        Processo p = repo.findById(u.id())
                .orElseThrow(() -> new IllegalArgumentException("Processo não encontrado"));
        p.setDescricao(u.descricao());
        repo.flush();
        log.info("Processo {} descrição atualizada", p.getId());
    }

    private void handleStatus(ProcessoCommand.ChangeStatus s) {
        Processo p = repo.findById(s.id())
                .orElseThrow(() -> new IllegalArgumentException("Processo não encontrado"));
        switch (s.novoStatus()) {
            case ARQUIVADO -> p.arquivar();
            case SUSPENSO  -> p.suspender();
            case ATIVO     -> p.reabrir();
        }
        repo.flush();
        log.info("Processo {} mudou status para {}", p.getId(), s.novoStatus());
    }
}
