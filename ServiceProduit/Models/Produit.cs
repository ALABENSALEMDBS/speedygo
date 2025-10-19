using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace ServiceProduit.Models
{
    public class Produit
    {
        //[BsonId]
        //[BsonRepresentation(BsonType.ObjectId)]
        public string? Id { get; set; }

       // [BsonElement("nom")]
        public string Nom { get; set; } = string.Empty;

        //[BsonElement("description")]
        public string Description { get; set; } = string.Empty;

        //[BsonElement("prix")]
        public decimal Prix { get; set; }

        //[BsonElement("quantite")]
        public int Quantite { get; set; }

      

    }
}
