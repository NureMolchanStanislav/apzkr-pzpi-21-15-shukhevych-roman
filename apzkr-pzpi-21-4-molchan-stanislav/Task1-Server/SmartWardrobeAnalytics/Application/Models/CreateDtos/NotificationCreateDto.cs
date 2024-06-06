namespace Application.Models.CreateDtos;

public class NotificationCreateDto
{
    public int Condition { get; set; }
    
    public string Title { get; set; }
    
    public string Description { get; set; }
    
    public string ItemId { get; set; }
}