package com.example.projektjava.mapper;

import com.example.projektjava.dataBase.DataBase;
import com.example.projektjava.enums.StatusEnum;
import com.example.projektjava.model.ConflictDTO;
import com.example.projektjava.model.ConflictForm;

import java.util.ArrayList;
import java.util.List;

public non-sealed class ConflictMapperImpl implements ConfilctMapper {

    public List<ConflictForm> mapDTOToForm(List<ConflictDTO> conflictsDTO) {
        List<ConflictForm> conflictForms = new ArrayList<>();
        for (ConflictDTO conflictDTO : conflictsDTO) {
            ConflictForm conflictForm = new ConflictForm();
            conflictForm.setId(conflictDTO.getId());
            conflictForm.setReporter(DataBase.getUserById(conflictDTO.getReporterId()).isPresent() ?
                                     DataBase.getUserById(conflictDTO.getReporterId()).get() : null);

            conflictForm.setUserInvolved(DataBase.getUsersInvolved(conflictDTO.getId()));
            conflictForm.setDescription(conflictDTO.getDescription());
            conflictForm.setStatus(StatusEnum.getNameById(conflictDTO.getStatusId()));
            conflictForm.setDate(conflictDTO.getDate());
            conflictForms.add(conflictForm);
        }
        return conflictForms;
    }


}
