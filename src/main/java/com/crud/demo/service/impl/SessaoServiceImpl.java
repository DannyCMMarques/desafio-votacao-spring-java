package com.crud.demo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.enums.DuracaoSessaoEnum;
import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.exceptions.sessao.DuracaoMinimaException;
import com.crud.demo.exceptions.sessao.SessaoJaFinalizadaException;
import com.crud.demo.exceptions.sessao.SessaoJaIniciadaException;
import com.crud.demo.exceptions.sessao.SessaoNaoCadastradaException;
import com.crud.demo.exceptions.sessao.SessaoNaoIniciadaException;
import com.crud.demo.repositories.SessaoRepository;
import com.crud.demo.service.PautaService;
import com.crud.demo.service.SessaoService;
import com.crud.demo.service.dto.sessao.SessaoRequestDTO;
import com.crud.demo.service.dto.sessao.SessaoResponseDTO;
import com.crud.demo.service.mappers.SessaoMapper;
import com.crud.demo.service.utils.DuracaoSessaoUtils;
import com.crud.demo.specifications.SessaoSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessaoServiceImpl implements SessaoService {

    private final SessaoRepository sessaoRepository;
    private final SessaoMapper sessaoMapper;
    private final PautaService pautaService;

    @Override
    public SessaoResponseDTO criarSessao(SessaoRequestDTO dto) {

        this.verificarDuracao(dto.getDuracao(), dto.getUnidade());
        Double duracaoMin = DuracaoSessaoUtils.converterMinutos(dto.getDuracao(), dto.getUnidade());
        Pauta pauta = pautaService.buscarPautaNaoVotadaPorId(dto.getIdPauta());

        Sessao sessao = sessaoMapper.toEntity(dto, pauta);
        sessao.setDuracao(duracaoMin);
        sessao.setStatus(StatusSessaoEnum.NAO_INICIADA);
        Sessao salva = sessaoRepository.save(sessao);
        SessaoResponseDTO sessaoSalvaResponse = sessaoMapper.toResponseDTO(salva);
        return sessaoSalvaResponse;
    }

    @Override
    public Page<SessaoResponseDTO> listarSessoesComFiltro(int page, int size, String sortBy, String direction,
            Long pautaId, StatusSessaoEnum status) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, sort);

        Specification<Sessao> spec = Specification
                .where(SessaoSpecification.comPautaId(pautaId))
                .and(SessaoSpecification.comStatus(status));

        Page<Sessao> sessoes = sessaoRepository.findAll(spec, pageable);
        return sessoes.map(sessaoMapper::toDto);
    }

    @Override
    public SessaoResponseDTO buscarPorId(Long id) {
        Sessao sessao = sessaoRepository.findById(id)
                .orElseThrow(SessaoNaoCadastradaException::new);
        SessaoResponseDTO sessaoEncontradaResponse = sessaoMapper.toDto(sessao);
        return sessaoEncontradaResponse;
    }

    @Override
    public SessaoResponseDTO atualizarSessao(Long id, SessaoRequestDTO dto) {
        Sessao sessao = this.verificarSessaoJaIniciada(id);

        Pauta pauta = pautaService.buscarPautaNaoVotadaPorId(dto.getIdPauta());
        sessao.setPauta(pauta);
        this.verificarDuracao(dto.getDuracao(), dto.getUnidade());
        Double duracaoMin = DuracaoSessaoUtils.converterMinutos(dto.getDuracao(), dto.getUnidade());
        dto.setDuracao(duracaoMin);
        sessao.setDuracao(dto.getDuracao());
        Sessao atualizada = sessaoRepository.save(sessao);
        SessaoResponseDTO sessaoAtualizadaResponse = sessaoMapper.toResponseDTO(atualizada);
        return sessaoAtualizadaResponse;
    }

    @Override
    public void deletarSessao(Long id) {
        Sessao sessao = this.verificarSessaoJaIniciada(id);

        sessaoRepository.delete(sessao);
    }

    @Override
    public void verificarDuracao(Double duracao, DuracaoSessaoEnum unidade) {
        boolean eh30Segundos = duracao < 30 && unidade == DuracaoSessaoEnum.SEG;
        boolean ehMeioMinuto = duracao < 0.5 && unidade == DuracaoSessaoEnum.MIN;
        if (eh30Segundos || ehMeioMinuto) {
            throw new DuracaoMinimaException();
        }
    }

    @Override
    public Sessao verificarSessaoJaIniciada(Long id) {
        Sessao sessao = sessaoRepository.findById(id).orElseThrow(SessaoNaoCadastradaException::new);
        if (sessao.getStatus() != StatusSessaoEnum.NAO_INICIADA) {
            throw new SessaoJaIniciadaException();
        }
        return sessao;
    }

    @Override
    public Sessao verificarSessaoAberta(Long id) {
        Sessao sessao = sessaoRepository.findById(id).orElseThrow(SessaoNaoCadastradaException::new);
        StatusSessaoEnum statusSessao = sessao.getStatus();
        if (statusSessao == StatusSessaoEnum.NAO_INICIADA) {
            throw new SessaoNaoIniciadaException();
        } else if (statusSessao == StatusSessaoEnum.FINALIZADA) {
            throw new SessaoJaFinalizadaException();
        }
        return sessao;
    }
}
