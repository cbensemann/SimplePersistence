package nz.co.nomadconsulting.simplepersistence;

import java.io.Serializable;
import java.util.Locale;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;


public class NaturalPhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl implements Serializable {

    private static final long serialVersionUID = 2316060970957438019L;

    public static final NaturalPhysicalNamingStrategy INSTANCE = new NaturalPhysicalNamingStrategy();


    @Override
    public Identifier toPhysicalTableName(final Identifier name, final JdbcEnvironment context) {
        return name == null ? null : new Identifier(addUnderscores(name.getText()), name.isQuoted());
    }


    @Override
    public Identifier toPhysicalColumnName(final Identifier name, final JdbcEnvironment context) {
        return name == null ? null : new Identifier(addUnderscores(name.getText()), name.isQuoted());
    }


    @Override
    public Identifier toPhysicalSequenceName(final Identifier name, final JdbcEnvironment context) {
        return name == null ? null : new Identifier(addUnderscores(name.getText()), name.isQuoted());
    }
    
    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
        return name == null ? null : new Identifier(addUnderscores(name.getText()), name.isQuoted());
    }

    
    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
        return name == null ? null : new Identifier(addUnderscores(name.getText()), name.isQuoted());
    }
    

    protected static String addUnderscores(final String name) {
        final StringBuilder buf = new StringBuilder(name.replace('.', '_'));
        for (int i = 1; i < buf.length() - 1; i++) {
            if (Character.isLowerCase(buf.charAt(i - 1)) && Character.isUpperCase(buf.charAt(i)) && Character.isLowerCase(buf.charAt(i + 1))) {
                buf.insert(i++, '_');
            }
        }
        return buf.toString().toLowerCase(Locale.ROOT);
    }
}
