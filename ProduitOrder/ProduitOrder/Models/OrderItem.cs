
namespace ProduitOrder.Models
{
    public class OrderItem
    {
        
            public string ProductId { get; set; } = null!;
            public string ProductName { get; set; } = null!;
            public float UnitPrice { get; set; }
            public int Quantity { get; set; }
        
    }
}
