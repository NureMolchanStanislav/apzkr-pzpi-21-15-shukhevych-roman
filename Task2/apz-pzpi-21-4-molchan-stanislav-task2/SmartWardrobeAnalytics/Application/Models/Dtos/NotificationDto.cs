namespace Application.Models.Dtos;

public class NotificationDto
{
    public string Id { get; set; }
    
    public int Condition { get; set; }
    
    public string Title { get; set; }
    
    public string Description { get; set; }
    
    public string ItemId { get; set; }
}