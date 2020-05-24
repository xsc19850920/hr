package com.rs.hr.modules.sys.controller;

import cn.hutool.core.collection.ListUtil;
import com.rs.hr.modules.sys.entity.SysUser;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sxia
 * @Description: TODO(Controller公共组件)
 * @date 2017-6-23 15:07
 */
public abstract class AbstractController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected SysUser getUser() {
        return (SysUser) SecurityUtils.getSubject().getPrincipal();
    }

    protected String getUserId() {
        return getUser().getId();
    }

    private List<String> ignoreList = Arrays.asList("_search","nd","limit","page","sidx","order","_");
    protected Map<String,Object> ignoreParam(Map<String,Object> params ){
        return params.entrySet().stream()
                .filter((e) -> (!ignoreList.contains(e.getKey())))
                .collect(Collectors.toMap(
                        (e) -> (String) e.getKey(),
                        (e) -> e.getValue()
                ));
    }



}
