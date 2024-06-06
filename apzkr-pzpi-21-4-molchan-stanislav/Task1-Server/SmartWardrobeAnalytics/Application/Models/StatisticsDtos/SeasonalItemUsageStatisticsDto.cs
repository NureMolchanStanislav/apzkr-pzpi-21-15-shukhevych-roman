namespace Application.Models.StatisticsDtos;

public class SeasonalItemUsageStatisticsDto
{
    public string Season { get; set; }
    
    public int TotalUsages { get; set; }
    
    public Dictionary<string, int> ItemUsages { get; set; }
}