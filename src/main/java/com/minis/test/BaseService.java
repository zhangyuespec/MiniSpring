package com.minis.test;

import com.minis.beans.factory.annotation.Autowired;
import com.sun.xml.internal.rngom.parse.host.Base;
import lombok.Data;

/**
 * @author yue.zhang5
 * @otherThing BUG FREE PLEASE
 * @time 2023/6/21-17:02
 **/
@Data
public class BaseService {
    @Autowired
    private BaseBaseService baseBaseService;
}
