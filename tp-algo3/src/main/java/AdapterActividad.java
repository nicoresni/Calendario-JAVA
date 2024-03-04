import com.google.gson.*;

import java.lang.reflect.Type;

public class AdapterActividad implements JsonSerializer<Object>, JsonDeserializer<Object> {
    @Override
    public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject serializado = new JsonObject();
        serializado.add("tipo", new JsonPrimitive(src.getClass().getSimpleName()));
        serializado.add("propiedades", context.serialize(src, src.getClass()));
        return serializado;
    }
    @Override
    public Object deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String tipo = jsonObject.get("tipo").getAsString();
        JsonElement objeto = jsonObject.get("propiedades");

        Class<? extends Actividad> clase;
        switch (tipo) {
            case "Tarea" -> clase = Tarea.class;
            case "Evento" -> clase = Evento.class;
            default -> throw new RuntimeException("tipo inválido");
        }
        try {
            return context.deserialize(objeto, clase);
        } catch (JsonParseException jsonParseException){
            throw new JsonParseException("Tipo de elemento Invalido: " + clase, jsonParseException);
        }
    }
}