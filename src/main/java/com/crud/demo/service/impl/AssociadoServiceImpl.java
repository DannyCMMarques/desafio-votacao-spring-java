package com.crud.demo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.crud.demo.domain.Associado;
import com.crud.demo.exceptions.associado.AssociadoNaoEncontradoException;
import com.crud.demo.exceptions.associado.CPFJaCadastradoException;
import com.crud.demo.repositories.AssociadoRepository;
import com.crud.demo.service.AssociadoService;
import com.crud.demo.service.VotoService;
import com.crud.demo.service.dto.associado.AssociadoRequestDTO;
import com.crud.demo.service.dto.associado.AssociadoResponseDTO;
import com.crud.demo.service.mappers.AssociadoMapper;
import com.crud.demo.specifications.AssociadoSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssociadoServiceImpl implements AssociadoService {

    private final AssociadoRepository associadoRepository;
    private final AssociadoMapper associadoMapper;
    private final VotoService votoService;

    @Override
    public AssociadoResponseDTO cadastrarAssociado(AssociadoRequestDTO associadoRequestDTO) {
        this.verificarCPFUnico(associadoRequestDTO);
        Associado associadoEntity = associadoMapper.toEntity(associadoRequestDTO);
        Associado associadoSalvo = associadoRepository.save(associadoEntity);
        AssociadoResponseDTO associadoResponse = associadoMapper.toDTO(associadoSalvo);
        return associadoResponse;

    }

    @Override
    public AssociadoResponseDTO buscarAssociadoPorId(Long id) {

        Associado associado = associadoRepository.findById(id)
                .orElseThrow(() -> new AssociadoNaoEncontradoException());
        AssociadoResponseDTO associadoResponse = associadoMapper.toDTO(associado);
        return associadoResponse;
    }

    @Override
    public Page<AssociadoResponseDTO> listarTodosAssociados(int page, int size, String sortBy, String direction, String cpf) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, sort);
        Specification<Associado> spec = Specification.where(AssociadoSpecification.cpfContem(cpf));
        Page<Associado> associados = associadoRepository.findAll(spec, pageable);
        return associados.map(associadoMapper::toDTO);
    }


@Override
public AssociadoResponseDTO atualizarAssociado(Long id, AssociadoRequestDTO request) {
        Associado associadoAtualizar = associadoRepository.findById(id)
                .orElseThrow(() -> new AssociadoNaoEncontradoException());
        this.verificarCPFUnico(request, id);
        associadoAtualizar.setNome(request.getNome());
        associadoAtualizar.setCpf(request.getCpf());
        Associado associadoAtualizado = associadoRepository.save(associadoAtualizar);
        AssociadoResponseDTO associadoAtualizadoResponse = associadoMapper.toDTO(associadoAtualizado);
        return associadoAtualizadoResponse;
    }

    @Override
public void deletarAssociado(Long id) {
        Associado associado = associadoRepository.findById(id)
                .orElseThrow(() -> new AssociadoNaoEncontradoException());
        votoService.associadoJaVotou(id);
        associadoRepository.delete(associado);
    }

    private void verificarCPFUnico(AssociadoRequestDTO request) {
        associadoRepository.findByCpf(request.getCpf())
                .ifPresent(a -> {
                    throw new CPFJaCadastradoException();
                });
    }

    private void verificarCPFUnico(AssociadoRequestDTO request, Long id) {
        associadoRepository.findByCpf(request.getCpf())
                .filter(a -> !a.getId().equals(id))
                .ifPresent(a -> {
                    throw new CPFJaCadastradoException();
                });
    }

}
