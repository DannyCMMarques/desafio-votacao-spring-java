package com.crud.demo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.crud.demo.domain.Associado;
import com.crud.demo.repositories.AssociadoRepository;
import com.crud.demo.service.AssociadoService;
import com.crud.demo.service.dto.associado.AssociadoRequestDTO;
import com.crud.demo.service.dto.associado.AssociadoResponseDTO;
import com.crud.demo.service.mappers.AssociadoMapper;
import com.crud.demo.service.validacoes.AssociadoValidacaoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssociadoServiceImpl implements AssociadoService {

    private final AssociadoRepository associadoRepository;
    private final AssociadoValidacaoService associadoValidacaoService;
    private final AssociadoMapper associadoMapper;

    @Override
    public AssociadoResponseDTO cadastrarAssociado(AssociadoRequestDTO associadoRequestDTO) {
        associadoValidacaoService.validarCPFCadastro(associadoRequestDTO.getCpf());
        Associado associadoEntity = associadoMapper.toEntity(associadoRequestDTO);
        Associado associadoSalvo = associadoRepository.save(associadoEntity);
        AssociadoResponseDTO associadoResponse = associadoMapper.toDTO(associadoSalvo);
        return associadoResponse;

    }

    @Override
    public AssociadoResponseDTO buscarAssociadoPorId(Long id) {
        Associado associado = associadoValidacaoService.validarExistencia(id);
        AssociadoResponseDTO associadoResponse = associadoMapper.toDTO(associado);
        return associadoResponse;
    }

    @Override
    public Page<AssociadoResponseDTO> listarTodosAssociados(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
         int pageIndex = page < 1 ? 0 : page - 1;
        Pageable pageable = PageRequest.of(pageIndex, size, sort);
        Page<Associado> associadosEncontrados = associadoRepository.findAll(pageable);
        Page<AssociadoResponseDTO> associadosEncontradosResponse = associadosEncontrados.map(associadoMapper::toDTO);
        return associadosEncontradosResponse;

    }

    @Override
    public AssociadoResponseDTO atualizarAssociado(Long id, AssociadoRequestDTO request) {
        Associado associadoAtualizar = associadoValidacaoService.validarExistencia(id);

        associadoAtualizar.setNome(request.getNome());
        associadoAtualizar.setCpf(request.getCpf());
        associadoValidacaoService.validarCPFAtualizacao(associadoAtualizar.getCpf(), id);
        Associado associadoAtualizado = associadoRepository.save(associadoAtualizar);
        AssociadoResponseDTO associadoAtualizadoResponse = associadoMapper.toDTO(associadoAtualizado);
        return associadoAtualizadoResponse;
    }

    @Override
    public void deletarAssociado(Long id) {
        Associado associado = associadoValidacaoService.validarExistencia(id);

        associadoRepository.delete(associado);
    }
}
