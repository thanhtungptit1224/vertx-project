package org.example.request;

import lombok.Data;
import org.example.util.StringUtil;

@Data
public class BaseRequest {
    private int     pageIndex   = 1;
    private int     pageSize    = 10;
    private String  sort        = StringUtil.EMPTY;
}
