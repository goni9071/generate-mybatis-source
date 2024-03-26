package %packageName%.parameter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
[IMPORT]

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class %entityName%Param extends SearchParameter {
%classContents%
}
