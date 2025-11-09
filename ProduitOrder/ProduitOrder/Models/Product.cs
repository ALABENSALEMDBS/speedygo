using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System.Text.Json.Serialization;
namespace ProduitOrder.Models
{
    public class Product
    {
        [BsonId]
        [BsonRepresentation(BsonType.String)]
        public string Id { get; set; } = Guid.NewGuid().ToString();

        public string Name { get; set; } = null!;
        public string? Description { get; set; }
        public float Price { get; set; }
        public int StockQuantity { get; set; }

        [BsonRepresentation(BsonType.String)]
        [JsonConverter(typeof(JsonStringEnumConverter))]
        public Category Category { get; set; }

        public string? Image { get; set; }
        public string? PartnerName { get; set; }
        public double Weight { get; set; }

        [BsonRepresentation(BsonType.String)]
        [JsonConverter(typeof(JsonStringEnumConverter))]
        public ProductStatus Status { get; set; }

        public int PreviousSales { get; set; } = 0;
        public string? Prediction { get; set; }
    }
}
