package nz.co.nomadconsulting.simplepersistence;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitJoinColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyHbmImpl;


public class NaturalImplicitNamingStrategy extends ImplicitNamingStrategyLegacyHbmImpl {

    private static final long serialVersionUID = 8764749060823901748L;

    @Override
    public Identifier determineJoinColumnName(final ImplicitJoinColumnNameSource source) {
        final Identifier original = super.determineJoinColumnName(source);
        final String name = original.getText();
        // TODO why do some columns already have _id on them??
        return Identifier.toIdentifier(name.endsWith("Id") || name.endsWith("_id") ? name : name + "Id", original.isQuoted());
    }
}
