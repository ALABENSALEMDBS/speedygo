using Microsoft.AspNetCore.Mvc;
using ProduitOrder.Models;
using ProduitOrder.Service;

namespace ProduitOrder.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class OrderController : ControllerBase
    {
        private readonly IOrderService _service;

        public OrderController(IOrderService service)
        {
            _service = service;
        }

        [HttpPost("create")]
        public async Task<IActionResult> CreateOrder([FromBody] Order order)
        {
            try
            {
                // Validation basique
                if (order == null)
                    return BadRequest("La commande ne peut pas être nulle");

                if (order.UserId <= 0)
                    return BadRequest("ID utilisateur invalide");

                if (order.Items == null || !order.Items.Any())
                    return BadRequest("La commande doit contenir au moins un article");

                var result = await _service.CreateOrder(order);
                return Ok(result);
            }
            catch (Exception ex)
            {
                // Log l'exception pour le débogage
                Console.WriteLine($"Erreur création commande: {ex.Message}");
                return BadRequest(ex.Message);
            }
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(string id)
        {
            try
            {
                var order = await _service.GetOrderById(id);
                return order == null ? NotFound() : Ok(order);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet("user/{userId}")]
        public async Task<IActionResult> GetOrdersForUser(long userId)
        {
            try
            {
                var result = await _service.GetOrdersByUserId(userId);
                return Ok(result);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}