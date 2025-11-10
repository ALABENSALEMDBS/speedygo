using Microsoft.AspNetCore.Mvc;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
namespace ProduitOrder.Models

{
    public class Order
    {
        
        [BsonId]
        [BsonRepresentation(BsonType.String)]
        public string Id { get; set; } = Guid.NewGuid().ToString();

        public string? TrackingNumber { get; set; }

        public DateTime? EstimatedDeliveryDate { get; set; }

        public float Price { get; set; }

       /* [BsonRepresentation(BsonType.String)]
        public Priority Priority { get; set; }

        [BsonRepresentation(BsonType.String)]
        public PackageStatus Status { get; set; }*/

        public DateTime Date { get; set; } = DateTime.UtcNow;

        // Embedded list
        public List<OrderItem> Items { get; set; } = new();

        // User reference (from user MS)
        public long UserId { get; set; }
        public string? UserFirstName { get; set; }
        public string? UserLastName { get; set; }
    
}
}
