using ProduitOrder.Models;

namespace ProduitOrder.Service
{
    public interface IOrderService
    {
        Task<Order> CreateOrder(Order order);
        Task<Order> GetOrderById(string id);
        Task<List<Order>> GetOrdersByUserId(string userId);
        Task Update(Order order);
        

        // Obtenir toutes les commandes d'un utilisateur
        Task<List<Order>> GetOrdersByUserId(long userId);

        // Mettre à jour une commande (optionnel selon ton projet)
        

    }
}
