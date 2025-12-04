package jp.co.jc21ps.activity_management.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jp.co.jc21ps.activity_management.dto.JoinApprovalDataDto;
import jp.co.jc21ps.activity_management.dto.JoinApprovalDto;
import jp.co.jc21ps.activity_management.entity.JoinApprovalDataEntity;
import jp.co.jc21ps.activity_management.entity.JoinApprovalEntity;
import jp.co.jc21ps.activity_management.repository.JoinApprovalRepository;
import jp.co.jc21ps.activity_management.dto.JoinApprovalNameDto;

@Service
public class JoinApprovalService {
    private final JoinApprovalRepository paramRepository;

    public JoinApprovalService(JoinApprovalRepository paramRepository) {
        this.paramRepository = paramRepository;
    }

    // 初期画面表示用
    public JoinApprovalNameDto getJoinApprovalData(JoinApprovalDto paramDto) {

        // entityに値をセット
        JoinApprovalEntity paramEntity = new JoinApprovalEntity();
        paramEntity.setClubId(paramDto.getClubId());
        paramEntity.setUserId(paramDto.getUserId());
        paramEntity.setUserName(paramDto.getUserName());
        paramEntity.setClubName(paramDto.getClubName());

        String clubName = paramRepository.getClubName(paramEntity);

        List<JoinApprovalEntity> approvalLists = paramRepository.getJoinApprovalList(paramEntity);
        List<JoinApprovalDto> viewData = new ArrayList<>();

        // dtoに値をセット
        for (JoinApprovalEntity entity : approvalLists) {
            JoinApprovalDto dto = new JoinApprovalDto();
            dto.setClubId(entity.getClubId());
            dto.setUserId(entity.getUserId());
            dto.setUserName(entity.getUserName());
            dto.setClubName(entity.getClubName());
            dto.setLeaderFlg(entity.isLeaderFlg());
            viewData.add(dto);
        }

        JoinApprovalNameDto responseDto = new JoinApprovalNameDto();
        responseDto.setJoinApprovalDto(viewData);
        responseDto.setClubName(clubName);

        return responseDto;
    }

    // 例外が起こったときに自動でロールバックする
    @Transactional
    public void deleteRequestInfo(JoinApprovalDataDto paramDto) {

        // entityに値をセット
        JoinApprovalDataEntity joinApprovalDataEntity = new JoinApprovalDataEntity();
        joinApprovalDataEntity.setUserId(paramDto.getUserId());
        joinApprovalDataEntity.setClubId(paramDto.getClubId());
        joinApprovalDataEntity.setLeaderFlg(paramDto.isLeaderFlg());

        paramRepository.deleteRequestInfo(joinApprovalDataEntity);
    }

    // 例外が起こったときに自動でロールバックする
    @Transactional
    public void insertRequestInfo(JoinApprovalDataDto paramDto) {

        // entityに値をセット
        JoinApprovalDataEntity joinApprovalDataEntity = new JoinApprovalDataEntity();
        joinApprovalDataEntity.setUserId(paramDto.getUserId());
        joinApprovalDataEntity.setClubId(paramDto.getClubId());
        joinApprovalDataEntity.setLeaderFlg(paramDto.isLeaderFlg());

        paramRepository.insertRequestInfo(joinApprovalDataEntity);
    }

    // 承認処理：削除と登録を1つのトランザクションで実行
    // 例外が起こったときに自動でロールバックする
    @Transactional
    public void approveRequest(JoinApprovalDataDto paramDto) {

        // entityに値をセット
        JoinApprovalDataEntity joinApprovalDataEntity = new JoinApprovalDataEntity();
        joinApprovalDataEntity.setUserId(paramDto.getUserId());
        joinApprovalDataEntity.setClubId(paramDto.getClubId());
        joinApprovalDataEntity.setLeaderFlg(paramDto.isLeaderFlg());

        // 1. 申請情報を削除
        paramRepository.deleteRequestInfo(joinApprovalDataEntity);

        // 2. クラブメンバーに登録
        paramRepository.insertRequestInfo(joinApprovalDataEntity);
    }

}
