package com.crud.demo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.repositories.SessaoRepository;
import com.crud.demo.service.SessaoService;
import com.crud.demo.service.dto.sessao.SessaoRequestDTO;
import com.crud.demo.service.dto.sessao.SessaoResponseDTO;
import com.crud.demo.service.mappers.SessaoMapper;
import com.crud.demo.service.utils.DuracaoSessaoUtils;
import com.crud.demo.service.validacoes.PautaValidacaoService;
import com.crud.demo.service.validacoes.SessaoValidacaoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessaoServiceImpl implements SessaoService {

    private final SessaoRepository sessaoRepository;
    private final SessaoMapper sessaoMapper;
    private final PautaValidacaoService pautaValidacaoService;
    private final SessaoValidacaoService sessaoValidacaoService;

    @Override
    public SessaoResponseDTO criarSessao(SessaoRequestDTO dto) {
        sessaoValidacaoService.verificarDuracao(dto.getDuracao(), dto.getUnidade());
        Double duracaoMin = DuracaoSessaoUtils.converterMinutos(dto.getDuracao(), dto.getUnidade());

        Pauta pauta = pautaValidacaoService.verificarStatusNaoVotada(dto.getIdPauta());
        Sessao sessao = sessaoMapper.toEntity(dto, pauta);
        sessao.setDuracao(duracaoMin);
        sessao.setStatus(StatusSessaoEnum.NAO_INICIADA);
        Sessao salva = sessaoRepository.save(sessao);
        SessaoResponseDTO sessaoSalvaResponse = sessaoMapper.toResponseDTO(salva);
        return sessaoSalvaResponse;
    }

    @Override
    public Page<SessaoResponseDTO> listarSessoes(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        int pageIndex = page < 1 ? 0 : page - 1;

        Pageable pageable = PageRequest.of(pageIndex, size, sort);
        Page<Sessao> sessoesEncontradas = sessaoRepository.findAll(pageable);
        Page<SessaoResponseDTO> sessoesResponse = sessoesEncontradas.map(sessaoMapper::toDto);
        return sessoesResponse;
    }

    @Override
    public SessaoResponseDTO buscarPorId(Long id) {
        Sessao sessao = sessaoValidacaoService.validarEObterSessao(id);
        SessaoResponseDTO sessaoEncontradaResponse = sessaoMapper.toDto(sessao);
        return sessaoEncontradaResponse;
    }

    @Override
    public SessaoResponseDTO atualizarSessao(Long id, SessaoRequestDTO dto) {
        Sessao sessao = sessaoValidacaoService.validarAcao(id);
        Pauta pauta = pautaValidacaoService.verificarStatusNaoVotada(dto.getIdPauta());
        sessao.setPauta(pauta);
        sessaoValidacaoService.verificarDuracao(dto.getDuracao(), dto.getUnidade());
        Double duracaoMin = DuracaoSessaoUtils.converterMinutos(dto.getDuracao(), dto.getUnidade());
        dto.setDuracao(duracaoMin);
        sessao.setDuracao(dto.getDuracao());
        Sessao atualizada = sessaoRepository.save(sessao);
        SessaoResponseDTO sessaoAtualizadaResponse = sessaoMapper.toResponseDTO(atualizada);
        return sessaoAtualizadaResponse;
    }

    @Override
    public void deletarSessao(Long id) {
        Sessao sessao = sessaoValidacaoService.validarAcao(id);
        sessaoRepository.delete(sessao);
    }
}
