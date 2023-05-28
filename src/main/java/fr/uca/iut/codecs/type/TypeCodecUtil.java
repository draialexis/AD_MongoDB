package fr.uca.iut.codecs.type;

import fr.uca.iut.entities.Type;
import fr.uca.iut.utils.enums.TypeName;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;

public class TypeCodecUtil {
    public static Type extractType(Document typeDoc) {
        Type type = new Type();
        type.setName(TypeName.valueOf(typeDoc.getString("name")));
        List<TypeName> weakAgainst = typeDoc.getList("weakAgainst", String.class)
                                            .stream()
                                            .map(TypeName::valueOf)
                                            .collect(Collectors.toList());
        type.setWeakAgainst(weakAgainst);
        List<TypeName> effectiveAgainst = typeDoc.getList("effectiveAgainst",
                                                          String.class)
                                                 .stream()
                                                 .map(TypeName::valueOf)
                                                 .collect(Collectors.toList());
        type.setEffectiveAgainst(effectiveAgainst);
        return type;
    }
}
