package org.allobanktest.common.helpers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BaseResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -135584489115474388L;

    private int code;
    private String message;
    private T data;
    // Optional Error Messages if any
    private List<String> errors;
    private String serverTime;
}
