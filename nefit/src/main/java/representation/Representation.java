/**
 * Representation
 * Authors: João Silva, Henrique, Tifany Silva, Miguel Solans
 * Notes: Handles HTTP status code and data exchange
 */
package representation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Representation<T> {
    // HTTP Code Status
    private long code;
    // Data
    private T data;

    public Representation() {}

    public Representation(long code, T data) {
        this.code = code;
        this.data = data;
    }

    @JsonProperty
    public long getCode() {
        return code;
    }

    @JsonProperty
    public T getData() {
        return data;
    }
}
