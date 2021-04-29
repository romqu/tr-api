import org.jooq.codegen.DefaultGeneratorStrategy;
import org.jooq.meta.Definition;

public class EntityStrategy extends DefaultGeneratorStrategy {

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        return super.getJavaClassName(definition, mode) + "Entity";
    }
}
