namespace Application.Models.UpdateDtos;

public class NotificationUpdateDto
{
    public string Id { get; set; }
    
    public int Condition { get; set; }
    
    public string Title { get; set; }
    
    public string Description { get; set; }
    
    public string ItemId { get; set; }
}