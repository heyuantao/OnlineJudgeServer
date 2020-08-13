package cn.heyuantao.onlinejudgeserver.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author he_yu
 */
public class SysUserDetails implements UserDetails {

    private SysUser sysUser;

    public SysUserDetails(SysUser sysUser) {
        super();
        this.sysUser = sysUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        List<String> roleStringList = new ArrayList<>();
        roleStringList.add("ROLE_ADMIN");
        roleStringList.add("ROLE_USER");
        for(String roleString:roleStringList){
            grantedAuthorityList.add(new SimpleGrantedAuthority(roleString));
        }
        return grantedAuthorityList;
    }

    @Override
    public String getPassword() {
        return this.sysUser.getPassword();
    }

    @Override
    public String getUsername() {
        return this.sysUser.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
