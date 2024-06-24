package %packageName%.parameter.base;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
[IMPORT]


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class %entityName%BaseParam extends SearchParameter {
%classContents%
}
