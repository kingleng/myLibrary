package com.info.aegis.baselibrary.router;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leng on 2019/7/29.
 */
public class BaseManager {

    Map<String,BaseModule> baseModules;

    public BaseManager() {
        baseModules = new HashMap<>();
    }

    public Map<String,BaseModule> getBaseModules() {
        return baseModules;
    }

    public void addModule(String key, BaseModule module){
        baseModules.put(key,module);
    }

}
