package org.crud_ex.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.crud_ex.domain.MemberIdentityVO;
import org.crud_ex.domain.MemberVO;

@Mapper
public interface MemberIdentityMapper {

        void insert(MemberIdentityVO identity);

    MemberIdentityVO findByProviderAndUserId(
            @Param("provider") String provider,
            @Param("providerUserId") String providerUserId
    );

    MemberVO findMemberByProviderAndUserId(
            @Param("provider") String provider,
            @Param("providerUserId") String providerUserId
    );
}