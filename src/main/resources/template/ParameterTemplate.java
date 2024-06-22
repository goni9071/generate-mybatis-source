package %packageName%.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
[IMPORT]


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class %entityName%Param extends SearchParameter {
%classContents%
}
