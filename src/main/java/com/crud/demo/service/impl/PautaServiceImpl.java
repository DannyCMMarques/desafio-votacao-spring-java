package com.crud.demo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.crud.demo.domain.Pauta;
import com.crud.demo.repositories.PautaRepository;
import com.crud.demo.service.PautaService;
import com.crud.demo.service.dto.pauta.PautaRequestDTO;
import com.crud.demo.service.dto.pauta.PautaResponseDTO;
import com.crud.demo.service.dto.pauta.PautaResultadoDTO;
import com.crud.demo.service.mappers.PautaMapper;
import com.crud.demo.service.validacoes.PautaValidacaoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PautaServiceImpl implements PautaService {

    private final PautaRepository pautaRepository;
    private final PautaMapper pautaMapper;
    private final PautaValidacaoService pautaValidacao;

    @Override
    public PautaResponseDTO criarPauta(PautaRequestDTO pautaRequestDTO) {
        Pauta pautaEntity = pautaMapper.toEntity(pautaRequestDTO);
        Pauta pautaCriada = pautaRepository.save(pautaEntity);
        PautaResponseDTO pautaResponse = pautaMapper.toDto(pautaCriada);
        return pautaResponse;
    }

    @Override
    @Transactional
    public PautaResponseDTO atualizarPauta(Long id, PautaRequestDTO dto) {
        Pauta pautaExistente = pautaValidacao.verificarStatusNaoVotada(id);

        pautaExistente.setTitulo(dto.getTitulo());
        pautaExistente.setDescricao(dto.getDescricao());
        Pauta pautaAtualizada = pautaRepository.save(pautaExistente);
        PautaResponseDTO pautaResponseAtualizada = pautaMapper.toDto(pautaAtualizada);
        return pautaResponseAtualizada;
    }

    @Override
    public void deletarPauta(Long id) {
        pautaValidacao.verificarStatusNaoVotada(id);

        pautaRepository.deleteById(id);
    }

    @Override
    public Page<PautaResponseDTO> listarPautas(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PautaResponseDTO> pautasResponse = pautaRepository.findAll(pageable)
                .map(pautaMapper::toDto);
        return pautasResponse;
    }

    @Override
    public PautaResponseDTO buscarPorId(Long id) {
        Pauta pautaEncontrada = pautaValidacao.verificarStatusNaoVotada(id);
        return pautaMapper.toDto(pautaEncontrada);
    }

}
