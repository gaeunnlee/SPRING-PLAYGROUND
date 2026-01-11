package org.crud_ex.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.crud_ex.domain.MemberVO;

import java.util.List;

@Mapper
public interface MemberMapper {
    List<MemberVO> findAll();
    MemberVO findByMemberId(String memberId);
    MemberVO findByEmail(@Param("email") String email);
    int insert(MemberVO member);
    int update(MemberVO member);
    int delete(String memberId);
}
