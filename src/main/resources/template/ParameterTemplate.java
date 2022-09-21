package %packageName%.parameter;

import lombok.Data;
import lombok.EqualsAndHashCode;
[IMPORT]

@Data
@EqualsAndHashCode(callSuper = false)
public class %entityName%Param extends SearchParameter {
%classContents%
}
