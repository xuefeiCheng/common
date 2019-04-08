package com.ebupt.portal.shiro.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuInfo {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String menuId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String menuName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String url;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String parentMenu;

}
