package fr.uca.iut.codecs.move;

import com.mongodb.MongoClientSettings;
import fr.uca.iut.codecs.GenericCodec;
import fr.uca.iut.codecs.type.TypeCodecUtil;
import fr.uca.iut.entities.Move;
import fr.uca.iut.entities.Type;
import fr.uca.iut.utils.enums.MoveCategoryName;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

public class MoveCodec extends GenericCodec<Move> {
    private final Codec<Document> documentCodec;

    public MoveCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry()
                                                .get(Document.class);
    }

    @Override
    public void encode(BsonWriter writer, Move move, EncoderContext encoderContext) {
        Document doc = new Document();

        doc.put("_id", new ObjectId(move.getId()));

        doc.put("name", move.getName());

        doc.put("category", move.getCategory());

        doc.put("power", move.getPower());

        doc.put("accuracy", move.getAccuracy());

        Type moveType = move.getType();
        Document typeDoc = new Document();
        typeDoc.put("name",
                    moveType.getName()
                            .toString());
        typeDoc.put("weakAgainst", moveType.getWeakAgainst());
        typeDoc.put("effectiveAgainst", moveType.getEffectiveAgainst());
        doc.put("type", typeDoc);

        documentCodec.encode(writer, doc, encoderContext);
    }

    @Override
    public Class<Move> getEncoderClass() {
        return Move.class;
    }

    @Override
    public Move decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);
        Move move = new Move();

        move.setId(document.getObjectId("_id")
                           .toString());

        move.setName(document.getString("name"));

        move.setCategory(MoveCategoryName.valueOf(document.getString("category")));

        move.setPower(document.getInteger("power"));

        move.setAccuracy(document.getInteger("accuracy"));

        Document typeDoc = (Document) document.get("type");

        move.setType(TypeCodecUtil.extractType(typeDoc));

        return move;
    }
}
