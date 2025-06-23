package com.crud.demo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.enums.StatusPautaEnum;
import com.crud.demo.exceptions.pauta.PautaNaoCadastradaException;
import com.crud.demo.exceptions.pauta.PautaVotadaException;
import com.crud.demo.repositories.PautaRepository;
import com.crud.demo.service.PautaService;
import com.crud.demo.service.dto.pauta.PautaRequestDTO;
import com.crud.demo.service.dto.pauta.PautaResponseDTO;
import com.crud.demo.service.mappers.PautaMapper;
import com.crud.demo.specifications.PautaSpecification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PautaServiceImpl implements PautaService {

    private final PautaRepository pautaRepository;
    private final PautaMapper pautaMapper;

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
        Pauta pautaExistente = pautaRepository.findById(id)
                .orElseThrow(() -> new PautaNaoCadastradaException());

        pautaExistente.setTitulo(dto.getTitulo());
        pautaExistente.setDescricao(dto.getDescricao());
        Pauta pautaAtualizada = pautaRepository.save(pautaExistente);
        PautaResponseDTO pautaResponseAtualizada = pautaMapper.toDto(pautaAtualizada);
        return pautaResponseAtualizada;
    }

    @Override
    public void deletarPauta(Long id) {
        Pauta pautaExistente = pautaRepository.findById(id)
                .orElseThrow(() -> new PautaNaoCadastradaException());

        pautaRepository.delete(pautaExistente);
    }

    @Override
    public Page<PautaResponseDTO> listarPautas(int page, int size, String sortBy, String direction, String titulo,
            StatusPautaEnum status) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, sort);
        Specification<Pauta> spec = Specification
                .where(PautaSpecification.tituloContendo(titulo))
                .and(PautaSpecification.statusIgual(status));

        Page<Pauta> pautas = pautaRepository.findAll(spec, pageable);
        return pautas.map(pautaMapper::toDto);
    }

    @Override
    public PautaResponseDTO buscarPorId(Long id) {
        Pauta pautaExistente = pautaRepository.findById(id)
                .orElseThrow(() -> new PautaNaoCadastradaException());
        return pautaMapper.toDto(pautaExistente);
    }

    @Override
    public Pauta buscarPautaNaoVotadaPorId(Long id) {
        Pauta pauta = pautaRepository.findById(id)
                .orElseThrow(PautaNaoCadastradaException::new);

        if (pauta.getStatus() != StatusPautaEnum.NAO_VOTADA) {
            throw new PautaVotadaException();
        }
        return pauta;
    }
}
