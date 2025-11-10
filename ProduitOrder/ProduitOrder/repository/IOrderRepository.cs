using ProduitOrder.Models;

namespace ProduitOrder.repository
{
    public interface IOrderRepository
   {
    Task<Order> Add(Order order);
    Task<Order> GetById(string id);
    Task Update(Order order);
    Task<List<Order>> GetOrdersByUserId(long userId);
}
}
