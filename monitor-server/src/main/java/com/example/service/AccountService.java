package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.*;
import com.example.entity.vo.response.SubAccountVO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AccountService extends IService<Account>, UserDetailsService {
    Account findAccountByNameOrEmail(String text);

    String registerEmailVerifyCode(String type, String email, String address);

    String resetEmailAccountPassword(EmailResetVO info);

    String resetConfirm(ConfirmResetVO info);

    String changePassword(int id, ChangePasswordVO vo);

    String createSubAccount(CreateSubAccountVO vo);

    void deleteSubAccount(int uid);

    List<SubAccountVO> listSubAccount();

    String modifyEmail(int id, ModifyEmailVO vo);
}
