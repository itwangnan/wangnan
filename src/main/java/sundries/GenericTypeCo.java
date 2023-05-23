package sundries;

import java.util.List;
import java.util.stream.Collector;

public interface GenericTypeCo<R,T> {
     T flatMap(R r, T collector) ;

}
