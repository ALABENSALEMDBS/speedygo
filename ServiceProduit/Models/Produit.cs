using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace ServiceProduit.Models
{
    public class Produit
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string? Id { get; set; }

        public string Nom { get; set; } = string.Empty;
        public string Description { get; set; } = string.Empty;
        public string aaaa { get; set; } = string.Empty;
        public string llll { get; set; } = string.Empty;
        public decimal Prix { get; set; }
        public int Quantite { get; set; }
    }
}
