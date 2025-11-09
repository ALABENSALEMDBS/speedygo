namespace ProduitOrder.Models
{
    public class ProductInPanier
    {
        public string ProductId { get; set; } = null!;
        public string ProductName { get; set; } = null!;
        public float ProductPrice { get; set; }
        public int Quantity { get; set; }
    }
}
