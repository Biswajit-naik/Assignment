package com.expenses.tracker.ExpensesTracker.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUsermodel implements UserDetails{

    private final Usermodel user ;

    public CustomUsermodel(Usermodel user){
        this.user = user;
    }

    public Long getUserId(){
        return user.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(String.valueOf(user.getRole())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }
        

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return user.getEmail();
    }
    
}
