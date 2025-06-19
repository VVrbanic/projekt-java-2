package com.example.projektjava.mapper;

import com.example.projektjava.model.ConflictDTO;
import com.example.projektjava.model.ConflictForm;

import java.util.List;

public sealed interface ConfilctMapper permits ConflictMapperImpl {

    List<ConflictForm> mapDTOToForm(List<ConflictDTO> conflictsDTO);

}
