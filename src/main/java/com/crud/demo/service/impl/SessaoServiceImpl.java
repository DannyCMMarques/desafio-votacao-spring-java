package com.crud.demo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.repositories.PautaRepository;
import com.crud.demo.repositories.SessaoRepository;
import com.crud.demo.service.PautaValidacaoService;
import com.crud.demo.service.SessaoService;
import com.crud.demo.service.SessaoValidacaoService;
import com.crud.demo.service.dto.sessao.SessaoIniciadaResponseDTO;
import com.crud.demo.service.dto.sessao.SessaoRequestDTO;
import com.crud.demo.service.dto.sessao.SessaoResponseDTO;
import com.crud.demo.service.mappers.SessaoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessaoServiceImpl implements SessaoService {

    private final SessaoRepository sessaoRepository;
    private final PautaRepository pautaRepository;
    private final SessaoMapper sessaoMapper;
    private final PautaValidacaoService pautaValidacaoService;
    private final SessaoValidacaoService sessaoValidacaoService;

    @Override
    public SessaoResponseDTO criarSessao(SessaoRequestDTO dto) {

        Pauta pauta = pautaValidacaoService.verificarStatusNaoVotada(dto.getIdPauta());

        Sessao sessao = sessaoMapper.toEntity(dto, pauta);
        sessao.setStatus(StatusSessaoEnum.NAO_INICIADA);
        Sessao salva = sessaoRepository.save(sessao);
        SessaoResponseDTO sessaoSalvaResponse = sessaoMapper.toResponseDTO(salva);
        return sessaoSalvaResponse;
    }

    @Override
    public Page<SessaoResponseDTO> listarSessoes(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SessaoResponseDTO> sessoesEncontradas = sessaoRepository.findAll(pageable)
                .map(sessaoMapper::toResponseDTO);
        // TODO realizar lógica sobre responseDTO ou sessaoIniciadaResponse
        return sessoesEncontradas;
    }

    @Override
    public SessaoResponseDTO buscarPorId(Long id) {
        Sessao sessao = sessaoValidacaoService.validarEObterSessao(id);
        SessaoResponseDTO sessaoEncontradaResponse = sessaoMapper.toResponseDTO(sessao);
        return sessaoEncontradaResponse;
    }

    @Override
    public SessaoResponseDTO atualizarSessao(Long id, SessaoRequestDTO dto) {
        Sessao sessao = sessaoValidacaoService.validarAcao(id);
        Pauta pauta = pautaValidacaoService.verificarStatusNaoVotada(dto.getIdPauta());
        sessao.setPauta(pauta);
        sessao.setDuracao(dto.getDuracao());
        Sessao atualizada = sessaoRepository.save(sessao);
        SessaoResponseDTO sessaoAtualizadaResponse = sessaoMapper.toResponseDTO(atualizada);
        return sessaoAtualizadaResponse;
    }

    @Override
    public SessaoIniciadaResponseDTO atualizarStatusSessao(Sessao sessao) {
      sessao.setStatus(sessao.getStatus());
      sessao.setHorarioInicio(sessao.getHorarioInicio());
        Sessao atualizada = sessaoRepository.save(sessao);
        SessaoIniciadaResponseDTO sessaoAtualizadaResponse = sessaoMapper.toIniciadaResponseDTO(atualizada);
        return sessaoAtualizadaResponse;
    }

    @Override
    public void deletarSessao(Long id) {
        Sessao sessao = sessaoValidacaoService.validarAcao(id);
        sessaoRepository.delete(sessao);
    }
}
